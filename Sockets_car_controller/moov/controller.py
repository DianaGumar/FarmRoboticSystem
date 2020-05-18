import ASUS.GPIO as GPIO
from time import sleep

class Controller:

    def __init__(self,  in1, in2, en):
        self.in1 = in1
        self.in2 = in2
        self.en = en

        self.speeds = [10, 25, 50, 75]


    def setUp(self):
        # GPIO.setmode(GPIO.BCM)

        GPIO.setup(self.in1, GPIO.OUT)
        GPIO.setup(self.in2, GPIO.OUT)
        GPIO.setup(self.en, GPIO.OUT)
        GPIO.output(self.in1, GPIO.LOW)
        GPIO.output(self.in2, GPIO.LOW)
        self.p = GPIO.PWM(self.en, 1000)

        self.p.start(self.speeds[1])


    def stop(self):
        GPIO.output(self.in1, GPIO.LOW)
        GPIO.output(self.in2, GPIO.LOW)
        print("stop")


    def forward(self, time):
        print("forward")
        GPIO.output(self.in1, GPIO.HIGH)
        GPIO.output(self.in2, GPIO.LOW)
        sleep(time)
        self.stop()


    def backward(self, time):
        print("backward")
        GPIO.output(self.in1, GPIO.LOW)
        GPIO.output(self.in2, GPIO.HIGH)
        sleep(time)
        self.stop()


    def change_speed(self, speed):
        self.p.ChangeDutyCycle(self.speeds[speed])
        print("speed: " + str(self.speeds[speed]))


    def drop(self):
        GPIO.cleanup()
        print("GPIO Clean up")
