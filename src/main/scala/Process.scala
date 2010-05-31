class Process (cmd : String) {
  val process = Runtime.getRuntime().exec(cmd)
  val inLines = new scala.io.BufferedSource (process.getInputStream()).getLines()
  val os = process.getOutputStream()

  def readLine () : String = return inLines.next

  def write(s : String) {
    os.write(s.getBytes())
    os.flush()
  }

  def close() {
    os.close()
    process.destroy()
    process.waitFor()
  }
}
