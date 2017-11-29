package stardemo

trait Animation {
  def tick(): Unit
  def render(): Unit
}