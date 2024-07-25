from app import db
from sqlalchemy import Column, Integer, Float, ForeignKey, DateTime, func
from sqlalchemy.orm import relationship


class Patient(db.Model):
    __tablename__ = "patients"

    id = Column(Integer, autoincrement=True, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    weight = Column(Float, nullable=False)
    height = Column(Float, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    user = relationship("User", back_populates="patient")

    def __init__(self, user_id, weight, height):
        self.user_id = user_id
        self.weight = weight
        self.height = height

    def serialize(self):
        return {
            "patient_id": self.id,
            "weight": self.weight,
            "height": self.height,
            "user": self.user.serialize(),
        }

    def __repr__(self):
        return f"<Patient {self.id}>"
