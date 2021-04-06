package app;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A {@link SocketFactory} that delegates calls. Sockets can be configured after creation by
 * overriding {@link #configureSocket(Socket)}.
 */
public class DelegatingSocketFactory extends SocketFactory {
  private final SocketFactory delegate;

  public DelegatingSocketFactory(SocketFactory delegate) {
    this.delegate = delegate;
  }

  @Override public Socket createSocket() throws IOException {
    Socket socket = delegate.createSocket();
    return configureSocket(socket);
  }

  @Override public Socket createSocket(String host, int port) throws IOException {
    Socket socket = delegate.createSocket(host, port);
    return configureSocket(socket);
  }

  @Override public Socket createSocket(String host, int port, InetAddress localAddress,
      int localPort) throws IOException {
    Socket socket = delegate.createSocket(host, port, localAddress, localPort);
    return configureSocket(socket);
  }

  @Override public Socket createSocket(InetAddress host, int port) throws IOException {
    Socket socket = delegate.createSocket(host, port);
    return configureSocket(socket);
  }

  @Override public Socket createSocket(InetAddress host, int port, InetAddress localAddress,
      int localPort) throws IOException {
    Socket socket = delegate.createSocket(host, port, localAddress, localPort);
    return configureSocket(socket);
  }

  protected Socket configureSocket(Socket socket) throws IOException {
    // No-op by default.
    return socket;
  }
}