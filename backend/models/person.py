from app import db
from sqlalchemy import Column, ForeignKey, Integer, String, DateTime, func
from sqlalchemy.dialects.postgresql import SMALLINT
from sqlalchemy.orm import relationship


class Person(db.Model):
    __tablename__ = "persons"

    id = Column(Integer, autoincrement=True, primary_key=True)
    clinic_id = Column(Integer, ForeignKey("clinics.id"), nullable=False)
    name = Column(String(30), nullable=False)
    lastname = Column(String(30), nullable=False)
    phone_number = Column(String(20), nullable=True)
    document_type = Column(SMALLINT, nullable=False)
    document_number = Column(String(15), nullable=False)
    gender = Column(SMALLINT, nullable=False)
    born_date = Column(DateTime, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    user = relationship("User", back_populates="person")

    def __init__(
        self,
        clinic_id,
        name,
        lastname,
        document_type,
        document_number,
        gender,
        born_date,
        phone_number,
    ):
        self.clinic_id = clinic_id
        self.name = name
        self.lastname = lastname
        self.document_type = document_type
        self.document_number = document_number
        self.gender = gender
        self.born_date = born_date
        self.phone_number = phone_number

    def __repr__(self):
        return f"<Person {self.name}>"
