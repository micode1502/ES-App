from app import db
from sqlalchemy import Column, Integer, ForeignKey, DateTime, func, SmallInteger


class Invoice(db.Model):
    __tablename__ = "invoices"

    id = Column(Integer, autoincrement=True, primary_key=True)
    patient_id = Column(Integer, ForeignKey("patients.id"), nullable=False)
    appointment_id = Column(Integer, ForeignKey("appointments.id"), nullable=False)
    amount = Column(Integer, nullable=False)
    status = Column(SmallInteger, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, patient_id, appointment_id, amount, status):
        self.patient_id = patient_id
        self.appointment_id = appointment_id
        self.amount = amount
        self.status = status

    def serialize(self):
        return {
            "patient_id": self.patient_id,
            "appointment_id": self.appointment_id,
            "amount": self.amount,
            "status": self.status,
        }

    def __repr__(self):
        return f"<Invoice {self.id}>"
