package il.ac.technion.cs.softwaredesign.storage.users

class SecureUserStorage : IUserStorage {
    override fun getUserIdByUsername(username: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserIdToUsername(userId: Long, username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserIdByToken(token: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserIdToToken(userId: Long, token: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyValueByUserId(userId: Long, property: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyValueToUserId(userId: Long, property: String, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyListByUserId(userId: Long, property: String): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyListToUserId(userId: Long, property: String, listValue: List<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}