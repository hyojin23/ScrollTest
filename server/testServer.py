import socket
import random
import numpy as np
import cv2

# 본인 컴퓨터 IP 주소
IP = socket.gethostbyname(socket.getfqdn())
PORT = 8700
print('IP Address ', IP, ':', PORT)

# 소켓 객체를 생성합니다.
# 주소 체계(address family)로 IPv4, 소켓 타입으로 TCP 사용합니다.
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# bind 함수는 소켓을 특정 네트워크 인터페이스와 포트 번호에 연결하는데 사용됩니다.
# HOST는 hostname, ip address, 빈 문자열 ""이 될 수 있습니다.
# 빈 문자열이면 모든 네트워크 인터페이스로부터의 접속을 허용합니다.
# PORT는 1-65535 사이의 숫자를 사용할 수 있습니다.
server_socket.bind((IP, PORT))

# 서버가 클라이언트의 접속을 허용하도록 합니다.
server_socket.listen()
print("listening...")

x, y = 500, 800

try:
    client_socket, clinet_addr = server_socket.accept()
    print('Connected with ', clinet_addr)

    while True:
        x = x + random.randint(-50, 50)
        y = y + random.randint(-50, 50)
        click = 0
        cord = str(x) + '/' + str(y) + '/' + str(click) + '\n'

        # 문자열을 byte로 변환하여 클라이언트로 보냄
        client_socket.sendall(cord.encode('utf-8'))

        # 클라이언트로 보내는 좌표 String
        print('send data', cord)
        # model data
        # connect.sendall()

except Exception as e:
    print(e)
    print('Connecting Error')
    pass
finally:
    socket.close()
    print('End of the session')
