package hotkitchen.error

class InvalidEmailError : Exception("""{"status":"Invalid email"}""")
class InvalidPasswordError : Exception("""{"status":"Invalid password"}""")
class AuthenticationFailed : Exception("""{"status":"Authentication Failed"}""")
class EmailAlreadyRegistered : Exception("""{"status":"Email is already registered"}""")
class WrongIdFormatException : Exception("""{"status":"Wrong ID format"}""")
class WrongIdRangeException : Exception("""{"status":"Wrong ID range"}""")
class WrongEmailFormat : Exception("""{"status":"Wrong email format"}""")
class WrongPasswordFormat : Exception("""{"status":"Wrong password format"}""")
class UserNotFound : Exception("""{"status":"User not found"}""")
class BearerStrangeError : Exception("""{"Authorization":"Bearer something.very.strange"}""")