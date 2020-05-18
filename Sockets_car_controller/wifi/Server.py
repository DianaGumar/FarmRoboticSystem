import socket
import pickle

from time import sleep
# from moov.car_controller import Moove

class Server:

    max_clients = 10
    was_clients = 0
    port = 8089


    map = ""
    data = []


    def start(self):

        self.s= socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        ip_address = socket.gethostbyname_ex(socket.gethostname())
        self.s.bind(('', self.port))
        self.s.listen(self.max_clients)

        print("Server started at IP: " + str(ip_address) + " ; port: " + str(self.port))

        while self.was_clients < self.max_clients:
            self.connect_clients()

        self.s.close()



    def connect_clients(self):
        print("listen clients")
        self.clt, self.adr = self.s.accept()
        print(str(self.was_clients) + " connection to " + str(self.adr) + " established")
        # msg = self.clt.recv(64).decode("utf-8")
        #print(msg)

        self.clt.send(bytes("You connected", "utf-8"))
        # self.clt.send(bytes("You connected"))

        self.was_clients += 1

        while True:
           #try:
                msg = int(self.clt.recv(1024).decode("utf-8"))
                print(msg)
                if (msg == 1):
                    self.messagin_controller()
                    self.clt.close()
                    print("unconnected " + str(self.adr))
                    break
                elif(msg == 2):
                    # l = self.get_serialize()
                    l = self.get_data()
                    self.data.append(l)
                    self.clt.close()
                    print("unconnected " + str(self.adr))
                    break
                elif(msg == 3):
                    self.map_controller()
                    # self.map = self.get_serialize()
                    # print("unconnected " + str(adr))
                    break
                elif (msg == 4):
                    self.messagin_base()
                    self.clt.close()
                    print("unconnected " + str(self.adr))
                    break
                elif (msg == 0):
                    print("unconnected " + str(self.adr))
                    break
                else:
                    print("incorrect key. Try again")

           #except:
            #   print("unconnected (unsafe) " + str(adr))
            #   break





    def messagin_base(self):

        self.data_str = ","

        for d in self.data:
            self.data_str = self.data_str + d + ","

        self.data_str = self.data_str[1:-1]
        print(self.data_str)

        b = bytes((str)(self.data_str), "utf-8")

        self.clt.send(bytes((str)(b), "utf-8"))



    def get_data(self) -> str:
        string = self.clt.recv(1024).decode("utf-8")

        string = string.split(']')
        string = string[0]

        print("Data: " + string)

        return string


    def get_serialize(self):
        all_data = bytearray()

        while True:
            data = self.clt.recv(4096)
            if not data:
                break
            all_data += data


        obj = pickle.loads(all_data)
        print('Obj:' + str(obj))
        print('Obj:' + str(all_data))

        return obj


    def map_controller(self):
        string = self.clt.recv(2024).decode("utf-8")
        self.clt.close()
        print("unconnected " + str(self.adr))

        string = string.split(']')
        string = string[0] + "]"

        print("Path: " + string)

        string = string[1: -1]
        string = string.replace(" ", "")
        string = string.split(',')

        for val in string:
            if(val == "f"):
                val = val+"2"
            elif(val == "s"):
                val = val+"5"
            else:
                val = val+"90"
            print(val)
            self.controller(val)
            if(val == "s5"):
                print(val)
                self.connect_clients()
            sleep(4)


    def controller(self, msg):
        command = ''.join(x for x in msg if x.isalpha())
        time = ''.join(x for x in msg if x.isdigit())

        if (command == "s"):
            print("stop")
        elif (command == "f"):
            print("foward")
            # m.m_b.forward(time)
        elif (command == "b"):
            print("backward")
            # m.m_b.backward (time)
        elif (command == "l"):
            print("low")
            # m.m_b.change_speed(m.m_b.speeds[1])
        elif (command == "m"):
            print("medium")
            # m.m_b.change_speed(m.m_b.speeds[2])
        elif (command == "h"):
            print("high")
            # m.m_b.change_speed(m.m_b.speeds[3])
        elif (command == "rl"):
            print("rotate left")
            # m.rotate(1, time)
        elif (command == "rr"):
            print("rotate right")
            # m.rotate(0, time)


    def messagin_controller(self):

            start = True

            # m = Moove()
            # m.m_b.setUp()
            #
            # m.m_r.setUp()

            while start:
                msg = self.clt.recv(1024).decode("utf-8")

                print("Controller: " + msg)

                if (msg == "" or msg == "0"):
                    # m.end()
                    start = False
                else:
                    self.controller(msg)







