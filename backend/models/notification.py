from app import db
from sqlalchemy import Column, Integer, ForeignKey, DateTime, func, SmallInteger
from sqlalchemy.orm import relationship


class Notification(db.Model):
    __tablename__ = "notifications"

    id = Column(Integer, autoincrement=True, primary_key=True)
    patient_id = Column(Integer, ForeignKey("patients.id"), nullable=False)
    appointment_id = Column(Integer, ForeignKey("appointments.id"), nullable=False)
    status = Column(SmallInteger, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    appointment = relationship("Appointment", back_populates="notification")

    def __init__(self, patient_id, appointment_id, status):
        self.patient_id = patient_id
        self.appointment_id = appointment_id
        self.status = status

    def serialize(self):
        return {
            "patient_id": self.patient_id,
            "appointment_id": self.appointment_id,
            "status": self.status,
            "appointment": self.appointment.serialize(),
        }

    def __repr__(self):
        return f"<Notification {self.id}>"
