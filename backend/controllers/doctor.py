from flask import Blueprint, jsonify, request
from models.appointment import Appointment
from models.availability import Availability
from app import db
from models.clinic import Clinic

from models.doctor import Doctor
from models.person import Person
from models.role import Role
from models.user import User
from services.jwt import token_required
from sqlalchemy import func


doctors = Blueprint("doctors", __name__, url_prefix="/doctors")


@doctors.route("/", methods=["GET"])
@token_required
def get_doctors(current_user):
    try:
        doctors = Doctor.query.all()
        return jsonify([doctor.serialize() for doctor in doctors]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/", methods=["POST"])
@token_required
def create_doctor(current_user):
    try:
        data = request.get_json()
        # Extract user data from the request
        username = data.get("username")
        password = data.get("password")
        name = data.get("name")
        lastname = data.get("lastname")
        document_type = data.get("document_type")
        document_number = data.get("document_number")
        gender = data.get("gender")
        born_date = data.get("born_date")
        phone_number = data.get("phone_number")
        specialty = data.get("specialty")
        med_license = data.get("med_license")

        # Validate the presence of required fields
        if not (
            username
            and password
            and name
            and lastname
            and document_type
            and document_number
            and gender
            and born_date
            and phone_number
            and specialty
            and med_license
        ):
            return jsonify({"message": "Missing required fields"}), 400

        # Check if the username is already taken
        user = User.query.filter_by(username=username).first()
        if user:
            return jsonify({"message": "Username is already taken"}), 409

        # Create the person in the database

        clinic = Clinic.query.first()

        person_obj = Person(
            clinic_id=clinic.id,
            name=name,
            lastname=lastname,
            document_type=document_type,
            document_number=document_number,
            gender=gender,
            born_date=born_date,
            phone_number=phone_number,
        )

        db.session.add(person_obj)
        db.session.commit()

        person_id = person_obj.id

        role = Role.query.where(Role.name == "user").first()

        user_obj = User(
            username=username,
            password=password,
            person_id=person_id,
            role_id=role.id,
        )

        db.session.add(user_obj)
        db.session.commit()

        doctor_obj = Doctor(
            user_id=user_obj.id,
            specialty=specialty,
            med_license=med_license,
        )

        db.session.add(doctor_obj)
        db.session.commit()

        return jsonify({"message": "Doctor created successfully"}), 201
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/<int:id>", methods=["GET"])
@token_required
def get_doctor(current_user, id):
    try:
        doctor = Doctor.query.get(id)

        if not doctor:
            return jsonify({"message": "Doctor does not exist"}), 404

        return jsonify(doctor.serialize()), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/full_name/<string:full_name>", methods=["GET"])
@token_required
def get_doctor_by_name(current_user, full_name):
    try:
        if len(full_name) < 3:
            return jsonify({"message": "Name too short"}), 400

        doctors = (
            Doctor.query.join(User)
            .join(Person)
            .filter(
                func.lower(Person.name + " " + Person.lastname).like(
                    func.lower(f"%{full_name}%")
                )
            )
            .all()
        )

        if not doctors:
            return jsonify({"message": "Doctor not found"}), 404

        return jsonify([doctor.serialize() for doctor in doctors]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/<int:id>/appointments", methods=["GET"])
@token_required
def get_doctor_appointments(current_user, id):
    try:
        appointments = Appointment.query.filter_by(doctor_id=id).all()
        return (
            jsonify([appointment.serialize() for appointment in appointments]),
            200,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/<int:id>/availability", methods=["GET"])
@token_required
def get_doctor_availability(current_user, id):
    try:
        availability = Availability.query.filter_by(doctor_id=id).all()
        return (
            jsonify([availability.serialize() for availability in availability]),
            200,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/specialty/<string:specialty>", methods=["GET"])
@token_required
def get_doctor_by_specialty(current_user, specialty):
    try:
        doctors = Doctor.query.filter_by(specialty=specialty).all()
        return jsonify([doctor.serialize() for doctor in doctors]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@doctors.route("/specialties", methods=["GET"])
@token_required
def get_doctor_specialties(current_user):
    try:
        specialties = Doctor.query.with_entities(Doctor.specialty).distinct().all()
        return jsonify([specialty[0] for specialty in specialties]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500
