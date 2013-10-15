object Timer {
  
  def interval(f: () => Unit, time: Int) = {
    while(true) { f(); Thread sleep time}
  }
  
  def timeout(f: () => Unit, time: Int) = {
    Thread sleep time;
    f();
  }
}