import socket
import pickle

s=socket.socket(socket.AF_INET, socket.SOCK_STREAM)

hostname = socket.gethostname()
ip_address = socket.gethostbyname(hostname)


datas = []

# print("Enter IP")
# ip_address = input()
# print("Enter port")
# port = input()

print("Wait..")

s.connect(("tinkerboard", 8089))
#s.connect(("162.198.137.1", 8089))
# s.connect(("DESKTOP-TDHMSR5", 8089))
# s.connect(("Admin-ПК", 9090))

msg=s.recv(1024)
print(msg.decode("utf-8"))

clients = ["exit - 0", "controller - 1", "data - 2", "map - 3", "base - 4"]

print("Choose your kind: ")
print(clients)
kind = int(input())
s.send(bytes(str(kind), "utf-8"))

if (kind == 1):
    while True:
        msg = input()
        s.send(bytes(str(msg), "utf-8"))
        if (msg == "exit"):
            break
elif (kind == 2):
    obj = [2, 10, "hi"]
    # сериализуем сложный объект
    data = pickle.dumps(obj)
    s.sendall(data)

elif (kind == 3):
    obj = "[f2,f2,rr90,rr90,rl90,f]"
    s.send(bytes(obj, "utf-8"))
    # msg = input()
    # s.send(bytes(str(msg), "utf-8"))
    # сериализуем сложный объект
    # data = pickle.dumps(obj)
    # s.sendall(data)
elif (kind == 4):
    all_data = bytearray()

    data = s.recv(4096)
    all_data += data

    # десериализуем
    obj = pickle.loads(all_data)
    print("Data: ", obj)
    datas = obj


s.send(bytes("0", "utf-8"))
s.close()
# реализовать переключение на режим- сервер отправляет, клиент слушает
