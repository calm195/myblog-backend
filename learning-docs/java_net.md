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
