import socket               

s = socket.socket()        
host = socket.gethostname() 
port = 12345                

s.connect((host, port))

while True:
	msg = raw_input("Digite uma mensagem para o servidor: ")
	s.send(msg)
	print('Mensagem enviada pelo servidor: ' + s.recv(1024))
	
s.close                     
