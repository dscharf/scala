object Cake {
	trait ComponentOne {
		def once
	}
	
	trait ComponentTwo {
		def twice
	}
	
	trait ModuleOne { this: ModuleMix =>
		class ImplementationOne extends ComponentOne {
			def once = println("once")
		}
		val one = new ImplementationOne
	}
	
	trait ModuleTwo { this: ModuleMix =>
		class ImplementationTwo extends ComponentTwo {
			def twice = println("twice")
		}
		val two = new ImplementationTwo
	}
	
	trait ModuleMix extends ModuleOne with ModuleTwo
	
	
	
}