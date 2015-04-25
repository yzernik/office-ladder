package models

case class Ladder(id: Int,
                  name: String,
                  domain: String,
                  creator: String,
                  created: Int)

case class LadderInput(name: String)
