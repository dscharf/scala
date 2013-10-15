import scala.actors.Futures

object Main {
  
	def timerTest {
		Futures.future {Timer.interval(() => println("a"), 3000)}
		Timer.interval(() => println("b"), 3000)    
	}
	
	def cakeTest {
	
		class CakeClient extends Cake.ModuleMix {
			one.once
			
		}
		
		new CakeClient
	}
	
	def main(args: Array[String]): Unit = {
		cakeTest
	}
}