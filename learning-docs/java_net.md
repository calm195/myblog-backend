# Java Net

包名：`java.net`

1. NetworkInterface
   NetworkInterface类包含网络接口名称与IP地址列表。该类提供访问网卡设备的相关信息，如可以获取网卡名称、IP地址和子网掩码等。

   - `Enumeration<NetworkInterface> getNetworkInterfaces()`：获取本地机器的所有网络接口
   - `String getName()`：获取网络接口的名称，只能知道名称，但不能知道具体的设备信息
   - `String getDisplayName()`：获取网络接口的显示名称，包含设备的详细信息
   - `Enumeration<InetAddress> getInetAddresses()`：获取网络接口的所有IP地址
   - `boolean isUp()`：判断网络接口是否已经开启并运行
   - `boolean isLoopback()`：判断网络接口是否是回环接口
   - `boolean isPointToPoint()`：判断网络接口是否是点对点接口
   - `boolean isVirtual()`：判断网络接口是否是虚拟接口
   - `boolean supportsMulticast()`：判断网络接口是否支持多播
   - `String getIndex()`：获取网络接口的索引

2. InetAddress
    InetAddress类表示IP地址，包含IP地址的主机名和IP地址。该类提供了获取IP地址的方法，如可以获取主机名、IP地址、主机地址等。
    - `static InetAddress getByName(String host)`：根据主机名获取IP地址
    - `static InetAddress getLocalHost()`：获取本地主机的IP地址
    - `String getHostName()`：获取主机名
    - `String getHostAddress()`：获取主机地址
    - `byte[] getAddress()`：获取主机的IP地址
    - `boolean isReachable(int timeout)`：判断主机是否可达
    - `boolean isReachable(NetworkInterface netif, int ttl, int timeout)`：判断主机是否可达
    - `boolean isAnyLocalAddress()`：判断是否是任意本地地址
    - `boolean isLinkLocalAddress()`：判断是否是链路本地地址
    - `boolean isLoopbackAddress()`：判断是否是回环地址
    - `boolean isMCGlobal()`：判断是否是全球多播地址
    - `boolean isMCLinkLocal()`：判断是否是链路本地多播地址
    - `boolean isMCNodeLocal()`：判断是否是节点本地多播地址
    - `boolean isMCOrgLocal()`：判断是否是组织本地多播地址
    - `boolean isMCSiteLocal()`：判断是否是站点本地多播地址
    - `boolean isMulticastAddress()`：判断是否是多播地址
    - `boolean isSiteLocalAddress()`：判断是否是站点本地地址
    1. Inet4Address
        定义：`public final class Inet4Address extends InetAddress`

    2. Inet6Address
        定义：`public final class Inet6Address extends InetAddress`

        - `boolean isIPv4CompatibleAddress()`：判断是否是IPv4兼容地址

3. DatagramPacket
    DatagramPacket类表示数据包，包含数据和目标地址。该类提供了发送和接收数据包的方法，如可以发送数据包、接收数据包、获取数据包的数据等。
    - `DatagramPacket(byte[] buf, int length, InetAddress address, int port)`：创建数据包
    - `DatagramPacket(byte[] buf, int length)`：创建数据包
    - `DatagramPacket(byte[] buf, int length, SocketAddress address)`：创建数据包
    - `DatagramPacket(byte[] buf, int offset, int length, InetAddress address, int port)`：创建数据包
    - `DatagramPacket(byte[] buf, int offset, int length)`：创建数据包
    - `DatagramPacket(byte[] buf, int offset, int length, SocketAddress address)`：创建数据包
    - `byte[] getData()`：获取数据包的数据
    - `int getLength()`：获取数据包的长度
    - `SocketAddress getSocketAddress()`：获取数据包的地址
    - `void setData(byte[] buf, int offset, int length)`：设置数据包的数据
    - `void setLength(int length)`：设置数据包的长度
    - `void setSocketAddress(SocketAddress address)`：设置数据包的地址

4. DatagramSocket
    DatagramSocket类表示数据包套接字，包含数据包的发送和接收。该类提供了发送和接收数据包的方法，如可以发送数据包、接收数据包、关闭数据包套接字等。
    - `DatagramSocket()`：创建数据包套接字，绑定到本地地址，端口号随机
    - `DatagramSocket(int port)`：创建数据包套接字，绑定到指定端口
    - `DatagramSocket(int port, InetAddress laddr)`：创建数据包套接字
    - `DatagramSocket(SocketAddress bindaddr)`：创建数据包套接字，绑定到指定的套接字地址
    - `void send(DatagramPacket p)`：发送数据包
    - `void receive(DatagramPacket p)`：接收数据包
    - `void close()`：关闭数据包套接字
    - `void connect(InetAddress address, int port)`：连接数据包套接字
    - `void disconnect()`：断开数据包套接字
    - `boolean isClosed()`：判断数据包套接字是否已经关闭
    - `boolean isConnected()`：判断数据包套接字是否已经连接
    - `boolean isBound()`：判断数据包套接字是否已经绑定
    - `InetAddress getInetAddress()`：获取数据包套接字的地址
    - `int getPort()`：获取数据包套接字的端口
    - `int getLocalPort()`：获取数据包套接字的本地端口
    - `SocketAddress getLocalSocketAddress()`：获取数据包套接字的本地地址
    - `void setSoTimeout(int timeout)`：设置数据包套接字的超时时间
    - `int getSoTimeout()`：获取数据包套接字的超时时间
    - `void setSendBufferSize(int size)`：设置数据包套接字的发送缓冲区大小
    - `int getSendBufferSize()`：获取数据包套接字的发送缓冲区大小
    - `void setReceiveBufferSize(int size)`：设置数据包套接字的接收缓冲区大小
    - `int getReceiveBufferSize()`：获取数据包套接字的接收缓冲区大小
    - `void setReuseAddress(boolean on)`：设置数据包套接字的地址复用

5. ServerSocket

    ServerSocket类表示服务器套接字，包含服务器的地址和端口号。该类提供了接收客户端连接的方法，如可以接收客户端连接、获取客户端套接字等。
    - `ServerSocket(int port)`：创建服务器套接字，绑定到指定端口
    - `ServerSocket(int port, int backlog)`：创建服务器套接字，绑定到指定端口，指定客户连接请求队列的长度
    - `ServerSocket(int port, int backlog, InetAddress bindAddr)`：创建服务器套接字，绑定到指定端口，指定服务器要绑定的地址
    - `bind(SocketAddress endpoint)`：绑定服务器套接字到指定的套接字地址
    - `bind(SocketAddress endpoint, int backlog)`：绑定服务器套接字到指定的套接字地址，指定客户连接请求队列的长度
    - `boolean isBound()`：判断服务器套接字是否已经绑定，只要 ServerSocket 已经与一个端口绑定, 即使它已经被关闭, isBound() 方法也会返回 true
    - `Socket accept()`：接收客户端连接，从连接请求队列中取出一个客户的连接请求, 然后创建与客户连接的 Socket 对象, 并将它返回. 如果队列中没有连接请求, accept() 方法就会一直等待, 直到接收到了连接请求才返回.
    - `void close()`：关闭服务器套接字，服务器释放占用的端口, 并且断开与所有客户的连接
    - `boolean isClosed()`：判断服务器套接字是否已经关闭，只有执行了 ServerSocket 的 close()方法, isClosed() 方法才返回 true; 否则, 即使 ServerSocket 还没有和特定端口绑定, isClosed() 也会返回 false
    - `InetAddress getInetAddress()`：获取服务器套接字的地址
    - `int getLocalPort()`：获取服务器套接字的端口
    - `int getSoTimeout()`：获取服务器套接字的超时时间
    - `void setSoTimeout(int timeout)`：设置服务器套接字的超时时间
    - `void setReuseAddress(boolean on)`：设置服务器套接字的地址复用，如果网络上仍然有数据向旧的 ServerSocket 传输数据, 是否允许新的 ServerSocket 绑定到与旧的 ServerSocket 同样的端口上。
       > serverSocket.setReuseAddress(true) 方法必须在 ServerSocket 还没有绑定到一个本地端口之前调用, 否则执行 serverSocket.setReuseAddress(true) 方法无效. 此外, 两个共用同一个端口的进程必须都调用 serverSocket.setResuseAddress(true) 方法, 才能使得一个进程关闭 ServerSocket 后, 另一个进程的 ServerSocket 还能够立刻重用相同的端口.
    - `void setReceiveBufferSize(int size)`：设置服务器套接字的接收缓冲区大小
    - `int getReceiveBufferSize()`：获取服务器套接字的接收缓冲区大小，如果要设置大于 64 KB 的缓冲区, 必须在 ServerSocket 绑定到特定端口之前进行设置才有效

6. Proxy
    Proxy类表示代理服务器，包含代理服务器的类型和地址。该类提供了获取代理服务器的方法，如可以获取代理服务器的类型、地址等。
    - `Proxy(Proxy.Type type, SocketAddress sa)`：创建代理服务器
    - `Proxy.Type type()`：获取代理服务器的类型
    - `SocketAddress address()`：获取代理服务器的地址
