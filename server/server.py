import SocketServer


class Server(SocketServer.BaseRequestHandler):
	def handle(self):
		self.data = self.request.recv(1024).strip()
		print "%s wrote: %s" % (format(self.client_address[0]), self.data)
		self.request.sendall("i has gotten message <3")

if __name__=='__main__':
	print 'server starting up...'
	HOST, PORT = "localhost", 9999
	server = SocketServer.TCPServer((HOST, PORT), Server)
	print 'server running...'
	server.serve_forever()
