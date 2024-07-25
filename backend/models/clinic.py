from app import db
from sqlalchemy import Column, Integer, String, DateTime, func


class Clinic(db.Model):
    __tablename__ = "clinics"

    id = Column(Integer, autoincrement=True, primary_key=True)
    name = Column(String(50), nullable=False)
    address = Column(String(100), nullable=False)
    city = Column(String(50), nullable=False)
    postal_code = Column(String(10), nullable=False)
    phone = Column(String(50), nullable=False)
    state = Column(String(50), nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, name, address, city, postal_code, phone, state):
        self.name = name
        self.address = address
        self.city = city
        self.postal_code = postal_code
        self.phone = phone
        self.state = state

    def serialize(self):
        return {
            "name": self.name,
            "address": self.address,
            "city": self.city,
            "postal_code": self.postal_code,
            "phone": self.phone,
            "state": self.state,
        }

    def __repr__(self):
        return f"<Clinic {self.id}>"
