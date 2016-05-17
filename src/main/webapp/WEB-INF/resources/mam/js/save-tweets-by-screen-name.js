$(document).ready(function () {
    var stompClient = null;
    var socket = null;
    var connected = false;
    var responseContentBox = $('#response-content-box');

    $("#send-name").click(function () {
        connectAndSend();
    });

    /**
     * Create socket and connect with STOMP protocol
     */
    function connectAndSend() {
        //get the current context
        socket = new SockJS("/mam/twitter/ws-save-tweets-by-screen-name");        
        stompClient = Stomp.over(socket);
        stompClient.connect("guest", "guest", connectCallback, errorCallback);

        //wait until subscrption is complete TODO efactor in a callback
        setTimeout(send, 1000);
    }
    
    /**
     * Disconnet from the servers
     */
    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    /**
     * Callback function to be called when stomp client is connected to server
     */
    var connectCallback = function () {
        stompClient.subscribe('/twitter/topic/save-tweets-by-screen-name', function (message) {
            var m = JSON.parse(message.body);

            responseContentBox.append('<p>'+m.payload+'</p>');
            connected = true;
        });
    };

    /**
     * Callback function to be called when stomp client could not connect to 
     * server
     */
    var errorCallback = function (error) {
        // display the error's message header:
        alert(error.headers.message);
    };

    /**
     * Send a message to the connected topic of the web socket
     */
    function send() {
        if(connected) {
            var screenNames = $('#screenNames').val();
            stompClient.send("/twitter/ws-save-tweets-by-screen-name/"+screenNames, {}, "");
        } else {
            connectAndSend();
        }
    }
    
    $('#clear-content-box').click(function() {
        responseContentBox.empty();
        disconnect();
        connected = false;
    });
});
