from flask import Blueprint, jsonify, request
from models.appointment import Appointment
from models.doctor import Doctor
from models.invoice import Invoice
from models.notification import Notification
from models.patient import Patient
from app import db
from models.clinic import Clinic
from models.payment import Payment
from models.person import Person
from models.role import Role
from models.user import User
from services.jwt import token_required
from utils import validate_appointment_availability

patients = Blueprint("patients", __name__, url_prefix="/patients")


@patients.route("/", methods=["POST"])
@token_required
def create_patient(current_user):
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
        weight = data.get("weight")
        height = data.get("height")

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
            and weight
            and height
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

        patient_obj = Patient(
            user_id=user_obj.id,
            weight=weight,
            height=height,
        )

        db.session.add(patient_obj)
        db.session.commit()

        return jsonify({"message": "Patient created successfully"}), 201
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>", methods=["GET"])
@token_required
def get_patient(current_user, id):
    try:
        patient = Patient.query.get(id)

        if not patient:
            return jsonify({"message": "Patient does not exist"}), 404

        return jsonify(patient.serialize()), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>/appointments", methods=["GET"])
@token_required
def get_patient_appointments(current_user, id):
    try:
        appointments = Appointment.query.filter_by(patient_id=id).all()
        return jsonify([appointment.serialize() for appointment in appointments]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>/appointment", methods=["POST"])
@token_required
def create_patient_appointment(current_user, id):
    try:
        data = request.get_json()

        # Extract user data from the request
        doctor_id = data.get("doctor_id")
        date = data.get("date")
        duration = int(data.get("duration"))
        status = data.get("status")

        # Validate the presence of required fields
        if not (doctor_id and date and duration and status):
            return jsonify({"message": "Missing required fields"}), 400

        # Check if the doctor exists
        doctor = Doctor.query.get(doctor_id)
        if not doctor:
            return jsonify({"message": "Doctor does not exist"}), 404

        # Check if the patient exists
        patient = Patient.query.get(id)
        if not patient:
            return jsonify({"message": "Patient does not exist"}), 404

        # verify overlapping and availability

        is_valid, message = validate_appointment_availability(
            id, doctor_id, date, duration
        )

        if not is_valid:
            return jsonify({"message": message}), 400

        # Create the appointment
        appointment = Appointment(
            patient_id=id,
            doctor_id=doctor_id,
            date=date,
            duration=duration,
            status=status,
        )
        db.session.add(appointment)
        db.session.commit()

        # Create the notification
        notification = Notification(
            patient_id=id,
            appointment_id=appointment.id,
            status=0,
        )

        db.session.add(notification)
        db.session.commit()

        return jsonify(appointment.serialize()), 201
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>/invoice", methods=["POST"])
@token_required
def create_patient_invoice(current_user, id):
    try:
        data = request.get_json()

        # Extract user data from the request
        appointment_id = data.get("appointment_id")
        amount = data.get("amount")

        # Validate the presence of required fields
        if not (appointment_id and amount):
            return jsonify({"message": "Missing required fields"}), 400

        # Check if the appointment exists
        appointment = Appointment.query.get(appointment_id)
        if not appointment:
            return jsonify({"message": "Appointment does not exist"}), 404

        # Check if the patient exists
        patient = Patient.query.get(id)
        if not patient:
            return jsonify({"message": "Patient does not exist"}), 404

        # Create the invoice
        invoice = Invoice(
            patient_id=id,
            appointment_id=appointment_id,
            amount=amount,
            status=0,
        )
        db.session.add(invoice)
        db.session.commit()

        return jsonify(invoice.serialize()), 201
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>/invoices", methods=["GET"])
@token_required
def get_patient_invoices(current_user, id):
    try:
        invoices = Invoice.query.filter_by(patient_id=id).all()
        return jsonify([invoice.serialize() for invoice in invoices]), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/<int:id>/notifications", methods=["GET"])
@token_required
def get_patient_notifications(current_user, id):
    try:
        notifications = Notification.query.filter_by(patient_id=id).all()
        return (
            jsonify([notification.serialize() for notification in notifications]),
            200,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/notification/<int:id>", methods=["PUT"])
@token_required
def update_patient_notification(current_user, id):
    try:
        notification = Notification.query.get(id)
        if not notification:
            return jsonify({"message": "Notification does not exist"}), 404

        notification.status = 1

        db.session.commit()

        return jsonify(notification.serialize()), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


@patients.route("/payment", methods=["POST"])
@token_required
def handle_payment(current_user):
    try:
        data = request.get_json()

        # Extract user data from the request
        invoice_id = data.get("invoice_id")
        method = data.get("method")
        amount = data.get("amount")

        # Validate the presence of required fields
        if not invoice_id:
            return jsonify({"message": "Missing required fields"}), 400

        # Check if the invoice exists
        invoice = Invoice.query.get(invoice_id)
        if not invoice:
            return jsonify({"message": "Invoice does not exist"}), 404

        # Check if the invoice has been paid
        if invoice.status == 1:
            return jsonify({"message": "Invoice has already been paid"}), 400

        # Update the invoice status
        invoice.status = 1

        payment = Payment(invoice_id=invoice_id, method=method, amount=amount)

        db.session.add(payment)
        db.session.commit()

        return jsonify({"message": "Payment successful"}), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500
