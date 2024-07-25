from flask import Blueprint, jsonify
from models.clinic import Clinic

from services.jwt import token_required


clinics = Blueprint("clinics", __name__, url_prefix="/clinics")


@clinics.route("/", methods=["GET"])
@token_required
def get_clinics(current_user):
    try:
        clinic = Clinic.query.first()
        return jsonify(clinic.serialize()), 200
    except Exception as e:
        print(e)
        return jsonify({"message": "Internal server error"}), 500
