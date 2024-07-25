from app import db
from sqlalchemy import Column, Integer, String, DateTime, func, ForeignKey
from sqlalchemy.orm import relationship


class User(db.Model):
    __tablename__ = "users"

    id = Column(Integer, autoincrement=True, primary_key=True)
    username = Column(String(30), unique=True, nullable=False)
    password = Column(String(200), nullable=False)
    person_id = Column(Integer, ForeignKey("persons.id"), nullable=False)
    role_id = Column(Integer, ForeignKey("roles.id"), nullable=False)
    avatar = Column(String(200), nullable=True)
    status = Column(Integer, nullable=False, default=1)
    login_attempts = Column(Integer, nullable=False, default=0)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )
    person = relationship("Person", back_populates="user")
    doctor = relationship("Doctor", back_populates="user")
    patient = relationship("Patient", back_populates="user")

    def __init__(self, username, password, person_id, role_id):
        self.username = username
        self.password = password
        self.person_id = person_id
        self.role_id = role_id

    def serialize(self):
        return {
            "id": self.id,
            "username": self.username,
            "person": {
                "name": self.person.name,
                "lastname": self.person.lastname,
                "document_type": self.person.document_type,
                "document_number": self.person.document_number,
                "gender": self.person.gender,
                "phone_number": self.person.phone_number,
            },
        }

    def __repr__(self):
        return f"<User {self.username}>"
