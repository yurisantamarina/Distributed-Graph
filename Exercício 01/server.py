import socket               

s = socket.socket()         
host = socket.gethostname() 
port = 12345                
s.bind((host, port))        

s.listen(5)                 
c, addr = s.accept()    
print 'Got connection from', addr
 
while True:
	print('Mensagem enviada pelo cliente: ' + c.recv(1024))
	msg = raw_input("Digite uma mensagem para o cliente: ")
   	c.send(msg)
  
c.close()                
