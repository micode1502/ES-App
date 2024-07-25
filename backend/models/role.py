from app import db
from sqlalchemy import Column, Integer, SmallInteger, String, DateTime, func


class Role(db.Model):
    __tablename__ = "roles"

    id = Column(Integer, autoincrement=True, primary_key=True)
    name = Column(String(30), unique=True, nullable=False)
    image = Column(String(250), nullable=True)
    status = Column(SmallInteger, nullable=False, default=1)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, name):
        self.name = name

    def serialize(self):
        return {
            "id": self.id,
            "name": self.name,
            "image": self.image,
            "status": self.status,
            "created_at": self.created_at,
            "updated_at": self.updated_at,
        }

    def __repr__(self):
        return f"<Role {self.name}>"
