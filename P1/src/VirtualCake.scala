object VirtualCake {
	trait UserModule {
		type User <: UserLike
		
		trait UserLike { this: User =>
			def id: String
			def name: String
			
			def save(): Unit
		}
		
		def User(id: String, name: String): User
	}
	
	trait MongoUserModule extends UserModule { 
		class User(id: String, name: String) extends UserLike { 
			def id: String = "a"
			def name: String = "asd"
			def save: Unit = {
			}
		}
		
		def User(id: String, name: String) =
			new User(id, name)
	}
}