from app import db
from sqlalchemy import Column, Integer, ForeignKey, DateTime, String, func


class Record(db.Model):
    __tablename__ = "records"

    id = Column(Integer, autoincrement=True, primary_key=True)
    appointment_id = Column(Integer, ForeignKey("appointments.id"), nullable=False)
    description = Column(String(250), nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, appointment_id, description):
        self.appointment_id = appointment_id
        self.description = description

    def __repr__(self):
        return f"<Record {self.id}>"
