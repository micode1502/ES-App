from app import bcrypt


def generate_password_hash(password):
    hashed_password = bcrypt.generate_password_hash(password)
    return hashed_password.decode("utf-8")


# Example usage
password = "123456"
hashed_password = generate_password_hash(password)
print(hashed_password)