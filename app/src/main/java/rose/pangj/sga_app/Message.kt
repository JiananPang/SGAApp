package rose.pangj.sga_app

class Message(
    val text: String // message body
    , val sendBy: String // data of the user that sent this message
    , val receiveBy: String // is this message sent by us?
)
