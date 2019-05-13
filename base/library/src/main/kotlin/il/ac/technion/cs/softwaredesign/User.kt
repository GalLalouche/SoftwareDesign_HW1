package il.ac.technion.cs.softwaredesign

 class User constructor(val userName : String, val password : String, var token : String) {
     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (javaClass != other?.javaClass) return false

         other as User

         if (userName != other.userName) return false

         return true
     }

     override fun hashCode(): Int {
         return userName.hashCode()
     }
 }