import random
import time
from flask import Blueprint, jsonify, request
from app import db
import jwt
from models.clinic import Clinic
from models.patient import Patient
from models.role import Role
from models.user import User
from models.person import Person
import os
from dotenv import load_dotenv

from services.jwt import token_required


load_dotenv()

# user controller blueprint to be registered with api blueprint
users = Blueprint("users", __name__)


# route for login api/users/signup
@users.route("/signup", methods=["POST"])
def register_user():
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

        weight = random.randint(50, 100) + random.random()

        height = random.randint(150, 200) + random.random()

        patient_obj = Patient(
            user_id=user_obj.id,
            weight=weight,
            height=height,
        )

        db.session.add(patient_obj)
        db.session.commit()

        payload = {
            "iat": time.time(),
            "id": user_obj.id,
            "username": user_obj.username,
        }

        token = jwt.encode(payload, os.environ.get("SECRET_KEY"), algorithm="HS256")

        return (
            jsonify(
                {
                    "message": "User registered successfully",
                    "token": token,
                    "current_user": user_obj.serialize(),
                }
            ),
            201,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


# route for login api/users/
@users.route("/login", methods=["POST"])
def handle_login():
    try:
        data = request.get_json()

        # Extract user data from the request
        username = data.get("username")
        password = data.get("password")

        # Validate the presence of required fields
        if not (username and password):
            return jsonify({"message": "Missing required fields"}), 400

        # Check if the user exists
        user = User.query.filter_by(username=username).first()
        if not user:
            return jsonify({"message": "User does not exist"}), 404

        # Check if the password is correct
        if user.password != password:
            return jsonify({"message": "Invalid credentials"}), 401

        payload = {
            "iat": time.time(),
            "id": user.id,
            "username": user.username,
        }

        patient = Patient.query.filter_by(user_id=user.id).first()

        token = jwt.encode(payload, os.environ.get("SECRET_KEY"), algorithm="HS256")

        current_info = user.serialize()

        if patient:
            current_info = patient.serialize()

        return (
            jsonify(
                {
                    "message": "User logged in successfully",
                    "token": token,
                    "current_user": current_info,
                }
            ),
            200,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500


# route for login api/users/
@users.route("/", methods=["GET"])
@token_required
def get_users(current_user):
    try:
        # get users join persons
        users = User.query.join(Person).all()
        # return users
        return (
            jsonify(
                {
                    "users": [user.serialize() for user in users],
                    "user": current_user.serialize(),
                }
            ),
            200,
        )
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500
