import random
from app import db
from sqlalchemy import Column, Double, Integer, ForeignKey, DateTime, String, func
from sqlalchemy.orm import relationship


class Doctor(db.Model):
    __tablename__ = "doctors"

    id = Column(Integer, autoincrement=True, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    specialty = Column(String(50), nullable=False)
    med_license = Column(String(50), nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    salary_hour = Column(Double, nullable=True, default=100.0)
    rating = Column(Double, nullable=True, default=random.uniform(4, 5))

    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    user = relationship("User", back_populates="doctor")

    def __init__(self, user_id, specialty, med_license):
        self.user_id = user_id
        self.specialty = specialty
        self.med_license = med_license

    def serialize(self):
        return {
            "doctor_id": self.id,
            "specialty": self.specialty,
            "med_license": self.med_license,
            "salary_hour": self.salary_hour,
            "rating": self.rating,
            "user": self.user.serialize(),
        }

    def __repr__(self):
        return f"<Doctor {self.id}>"
