from sqlalchemy import and_, or_
from datetime import datetime, timedelta
from models.appointment import Appointment
from models.availability import Availability


def validate_appointment_availability(patient_id, doctor_id, date, duration):
    try:
        # Convert the input date string to a datetime object
        appointment_date = datetime.strptime(date, "%Y-%m-%d %H:%M")

        # Check if the doctor is available on the specified day and time
        doctor_availability = Availability.query.filter(
            Availability.doctor_id == doctor_id,
            Availability.day == appointment_date.weekday() + 2,
            Availability.hour_start <= appointment_date.hour,
            Availability.hour_start + Availability.duration / 60
            > appointment_date.hour,
        ).first()

        if not doctor_availability:
            return False, "Doctor is not available at the specified time"

        appointments = Appointment.query.all()

        # check overlapping appointments
        for appointment in appointments:
            if appointment.doctor_id == doctor_id:
                if (
                    appointment.date <= appointment_date
                    and appointment.date + timedelta(minutes=appointment.duration)
                    > appointment_date
                ):
                    return False, "Overlapping appointment for the same doctor"
                elif (
                    appointment.date >= appointment_date
                    and appointment.date
                    < appointment_date + timedelta(minutes=duration)
                ):
                    return False, "Overlapping appointment for the same doctor"
            if appointment.patient_id == patient_id:
                if (
                    appointment.date <= appointment_date
                    and appointment.date + timedelta(minutes=appointment.duration)
                    > appointment_date
                ):
                    return False, "Overlapping appointment for the same patient"
                elif (
                    appointment.date >= appointment_date
                    and appointment.date
                    < appointment_date + timedelta(minutes=duration)
                ):
                    return False, "Overlapping appointment for the same patient"

        return (
            True,
            "Appointment is available and does not overlap with existing appointments",
        )
    except Exception as e:
        print(e)
        return False, "Error during availability validation"
