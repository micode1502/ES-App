from app import db
from sqlalchemy import Column, Integer, ForeignKey, DateTime, func


class Availability(db.Model):
    __tablename__ = "availability"

    id = Column(Integer, autoincrement=True, primary_key=True)
    doctor_id = Column(Integer, ForeignKey("doctors.id"), nullable=False)
    day = Column(Integer, nullable=False)
    hour_start = Column(Integer, nullable=False)
    duration = Column(Integer, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, doctor_id, day, hour_start, duration):
        self.doctor_id = doctor_id
        self.day = day
        self.hour_start = hour_start
        self.duration = duration

    def serialize(self):
        return {
            "doctor_id": self.doctor_id,
            "day": self.day,
            "hour_start": self.hour_start,
            "duration": self.duration,
        }

    def __repr__(self):
        return f"<Availability {self.id}>"
