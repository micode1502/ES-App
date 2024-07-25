from app import db, bcrypt, app
from faker import Faker
from models.clinic import Clinic
from models.person import Person
from models.role import Role
from models.user import User
from models.doctor import Doctor
from models.patient import Patient
from models.availability import Availability
from models.appointment import Appointment
from models.invoice import Invoice
from models.payment import Payment


# Create a Faker instance
fake = Faker()


def create_fake_data():
    with app.app_context():
        try:
            # Create fake clinics
            clinics = [
                Clinic(
                    name=fake.company(),
                    address=fake.address(),
                    city=fake.city(),
                    postal_code=fake.zipcode(),
                    phone=fake.phone_number(),
                    state=fake.state(),
                )
                for _ in range(2)
            ]
            db.session.add_all(clinics)
            db.session.commit()

            # Create fake roles
            roles = [Role(name="user"), Role(name="admin")]
            db.session.add_all(roles)
            db.session.commit()

            # Create fake persons
            persons = [
                Person(
                    clinic_id=fake.random_element(elements=clinics).id,
                    name=fake.first_name(),
                    lastname=fake.last_name(),
                    document_type=fake.random_int(min=1, max=3),
                    document_number=fake.unique.random_number(digits=8),
                    gender=fake.random_int(min=1, max=2),
                    born_date=fake.date_of_birth(),
                    phone_number=fake.phone_number()[:20],
                )
                for _ in range(10)
            ]
            db.session.add_all(persons)
            db.session.commit()

            # Create fake users
            users = []
            for person in persons:
                user = User(
                    username=fake.user_name(),
                    password=bcrypt.generate_password_hash("12345").decode("utf-8"),
                    person_id=person.id,
                    role_id=fake.random_element(elements=roles).id,
                )
                users.append(user)
            db.session.add_all(users)
            db.session.commit()

            # Create 5 fake doctors for 5 users
            doctors = []
            for user in users if len(users) <= 5 else users[:5]:
                doctor = Doctor(
                    user_id=user.id,
                    specialty=fake.job(),
                    med_license=fake.unique.random_number(digits=8),
                )
                doctors.append(doctor)

            db.session.add_all(doctors)
            db.session.commit()

            # Create fake availability
            availabilities = [
                Availability(
                    doctor_id=fake.random_element(elements=doctors).id,
                    day=fake.random_int(
                        min=1, max=6
                    ),  # Assuming 0-6 for days of the week
                    hour_start=fake.random_int(
                        min=8, max=16
                    ),  # Assuming 8-16 for working hours
                    duration=fake.random_int(min=1, max=2)
                    * 30,  # Assuming 30-minute slots
                )
                for _ in range(5)  # You can adjust the number of fake availabilities
            ]
            db.session.add_all(availabilities)
            db.session.commit()

            # Create fake patients for the other 5
            patients = []

            for user in users if len(users) <= 5 else users[5:]:
                patient = Patient(
                    user_id=user.id,
                    weight=fake.random_int(min=50, max=100),
                    height=fake.random_int(min=150, max=200),
                )
                patients.append(patient)
            db.session.add_all(patients)
            db.session.commit()

            # Create fake appointments
            appointments = [
                Appointment(
                    patient_id=fake.random_element(elements=patients).id,
                    doctor_id=fake.random_element(elements=doctors).id,
                    date=fake.date_time_this_month(),
                    duration=fake.random_int(min=15, max=120),
                    status=fake.random_int(min=0, max=1),
                )
                for _ in range(2)
            ]
            db.session.add_all(appointments)
            db.session.commit()

            # Create fake invoices
            invoices = [
                Invoice(
                    patient_id=fake.random_element(elements=patients).id,
                    appointment_id=fake.random_element(elements=appointments).id,
                    amount=fake.random_int(min=50, max=500),
                    status=fake.random_int(min=0, max=1),
                )
                for _ in range(2)
            ]
            db.session.add_all(invoices)
            db.session.commit()

            # Create fake payments
            payments = [
                Payment(
                    invoice_id=fake.random_element(elements=invoices).id,
                    method=fake.random_int(min=0, max=1),
                    amount=fake.random_int(min=50, max=500),
                )
                for _ in range(2)
            ]
            db.session.add_all(payments)
            db.session.commit()

            print("Fake data inserted successfully.")
        except Exception as e:
            print(f"Error inserting fake data: {e}")
        finally:
            db.session.close()


if __name__ == "__main__":
    create_fake_data()
