package ricm.nio.babystep2;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReaderAutomata {

    private static final int INBUFFER_SZ = 2048;

    enum State {
        READING_LENGTH,
        READING_MSG
    } ;

    State state = State.READING_LENGTH ;
    // Buffers for incoming messages
    ByteBuffer inBuffer;

    // length of the buffer
    ByteBuffer lengthMsg;

    // get the socket channel on which the incoming data waits to be received
    SocketChannel sc ;

    //the length of the message
    int length;

    // register a write interest for the given client socket channel
    SelectionKey key;

    public ReaderAutomata(SocketChannel sc, Selector selector) {
        this.sc = sc;
        // register a write interest for the given client socket channel
        key = sc.keyFor(selector);
    }

    /**
     * Handle incoming data
     *
     * @param sc of the SocketChannel on which the incoming data waits to be received
     */
    public void handleRead() throws IOException {

        byte[] data = null;
        int length;

        if( state == State.READING_LENGTH ){
            //System.out.println( "READING LENGTH");
            //read the length
            lengthMsg = ByteBuffer.allocate( 4 );
            int n = sc.read( lengthMsg );
            if( n == -1 ){
                sc.close();
                return;
            }
            if ( lengthMsg.remaining() == 0 ) {
                System.out.println("all bytes receive");
                // process the received length message
                data = new byte[lengthMsg.position()];
                lengthMsg.rewind();
                lengthMsg.get(data,0,data.length);
                length = Integer.valueOf( new String(data).trim() );
                processMsg( data );

                //process to allocate the buffer message
                inBuffer = ByteBuffer.allocate( length );
                state = State.READING_MSG;
            }else{
                //System.out.println("not all byte receive");
            }
        }

        while ( state == State.READING_MSG ){
            //System.out.println( "READING MSG");
            int n = sc.read( inBuffer );
            if( n == -1 ){
                sc.close();
                return;
            }
            if ( inBuffer.remaining() == 0 ) {
                //System.out.println("all byte receive");
                state = State.READING_LENGTH;
                //key.interestOps(SelectionKey.OP_WRITE);
            }else{
                //System.out.println("not all byte receive");
            }
            // process the received data
            data = new byte[inBuffer.position()];
            inBuffer.rewind();
            inBuffer.get(data,0,data.length);
            processMsg( data );

            // echo back the same message to the client
            //send(sc, data, 0, data.length);
        }
    }

    public void processMsg( byte[] data){
        String msg = new String(data, Charset.forName("UTF-8"));
        System.out.println("NioServer received: " + msg);
    }



}
