package object test {
	trait GameCycle {
		def init()
	}
	trait LifeCycle {
		def start()
		def shutdown()
	}
	trait Executable {
		def execute()
	}
	trait Runnable {
		def run(step: Float)
	}
	
	trait Graphics
	
	trait Module
	trait Visual extends Module {
		def draw(g: Graphics)
	}
	trait Physical extends Module
	trait Logical extends Module
	trait Flag extends Module
	
	
	def d(){
		
	}

}
