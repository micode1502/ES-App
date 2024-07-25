from flask import Blueprint
from controllers.user import users
from controllers.patient import patients
from controllers.doctor import doctors
from controllers.clinic import clinics

# main blueprint to be registered with application
api = Blueprint("api", __name__)

# register user with api blueprint
api.register_blueprint(users, url_prefix="/users")
api.register_blueprint(patients, url_prefix="/patients")
api.register_blueprint(doctors, url_prefix="/doctors")
api.register_blueprint(clinics, url_prefix="/clinics")
