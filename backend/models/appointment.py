from app import db
from sqlalchemy import Column, Integer, ForeignKey, func
from sqlalchemy.dialects.postgresql import TIMESTAMP, SMALLINT
from sqlalchemy.orm import relationship


class Appointment(db.Model):
    __tablename__ = "appointments"

    id = Column(Integer, autoincrement=True, primary_key=True)
    patient_id = Column(Integer, ForeignKey("patients.id"), nullable=False)
    doctor_id = Column(Integer, ForeignKey("doctors.id"), nullable=False)
    date = Column(TIMESTAMP, nullable=False)
    duration = Column(Integer, nullable=False)
    status = Column(SMALLINT, nullable=False)
    created_at = Column(TIMESTAMP, default=func.current_timestamp())
    updated_at = Column(
        TIMESTAMP,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    notification = relationship("Notification", back_populates="appointment")

    def __init__(self, patient_id, doctor_id, date, duration, status):
        self.patient_id = patient_id
        self.doctor_id = doctor_id
        self.date = date
        self.duration = duration
        self.status = status

    def serialize(self):
        return {
            "id": self.id,
            "patient_id": self.patient_id,
            "doctor_id": self.doctor_id,
            "date": self.date,
            "duration": self.duration,
            "status": self.status,
        }

    def __repr__(self):
        return f"<Appointment {self.id}>"
