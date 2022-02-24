package ricm.nio.babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class WriterAutomata {

    private static final int INBUFFER_SZ = 2048;

    enum State {
        WRITING_LENGTH,
        WRITING_MSG,
        WRITING_IDLE
    } ;

    State state = State.WRITING_IDLE;

    // Buffers for incoming messages
    ByteBuffer outBuffer;

    // get the socket channel on which the incoming data waits to be received
    SocketChannel sc ;

    // length of the buffer
    ByteBuffer lengthMsg;

    // register a write interest for the given client socket channel
    SelectionKey key;

    public WriterAutomata(SocketChannel sc, Selector selector) {
        this.sc = sc;

        // register a write interest for the given client socket channel
        key = sc.keyFor(selector);
    }

    public void handleWrite(ByteBuffer outBuffer) throws IOException {

        byte[] data = null;
        int length;
        this.sc.write(outBuffer);
        key.interestOps(SelectionKey.OP_READ);


//        if( state == State.WRITING_IDLE ){
//            state = State.WRITING_LENGTH;
//            //System.out.println( "READING LENGTH");
//            //write the length
//            lengthMsg = ByteBuffer.allocate( 4 );
//
//        }
//        state = State.WRITING_MSG;
//
//        while ( state == State.WRITING_MSG ){
//            //System.out.println( "READING MSG");
//            int n = sc.read( inBuffer );
//            if( n == -1 ){
//                sc.close();
//                return;
//            }
//            if ( inBuffer.remaining() == 0 ) {
//                //System.out.println("all byte receive");
//                state = ReaderAutomata.State.READING_LENGTH;
//            }else{
//                //System.out.println("not all byte receive");
//            }
//            // process the received data
//            data = new byte[inBuffer.position()];
//            inBuffer.rewind();
//            inBuffer.get(data,0,data.length);
//            processMsg( data );
//
//            // echo back the same message to the client
//            //send(sc, data, 0, data.length);
//        }
//    }
//
//    public void sendMsg( byte[] data){
//        String msg = new String(data, Charset.forName("UTF-8"));
//        System.out.println("NioServer received: " + msg);
//    }

    }
}
