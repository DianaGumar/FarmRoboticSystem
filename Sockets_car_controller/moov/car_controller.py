from moov.controller import Controller
import ASUS.GPIO as GPIO

class Moove():

    def __init__(self):
        self.m_b = Controller(24, 23, 25)
        self.m_r = Controller(27, 22, 17)


        GPIO.setmode(GPIO.BCM)
        self.m_b.setUp()
        # self.m_r.setUp()


    def rotate(self, side, deg):

        if(side == 1):
            self.m_r.forward(0.2)
        else:
            self.m_r.backward(0.2)

        self.m_b.backward(deg/45)


        if (side == 1):
            self.m_r.backward(0.1)
        else:
            self.m_r.forward(0.1)



    def end(self):
        self.m_b.drop()
        self.m_r.drop()
