	public static String getIp() throws Exception{
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static String getLocalIPAddress()throws Exception{
		String ipstr = null;
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			if(netInterface.getName().startsWith("eth")){
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if(ip != null && ip instanceof Inet4Address){
						ipstr = ip.getHostAddress();
					}
				}
			}
			
		}
		if(ipstr != null)
			return ipstr;
		else
			throw new Exception("Can not get local ip address!");
	}