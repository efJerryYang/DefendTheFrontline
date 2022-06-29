import socket
import json
import threading
import sys
import os

"""
 * sample code: https://blog.csdn.net/Olivia_2/article/details/124220255
"""


class Player:

    def __init__(self, ip) -> None:
        self.ip = ip
        self.fip = ""
        self.score = 0
        self.fscore = 0
        self.matched = False

    def __str__(self) -> str:
        return str(self.__dict__).replace('\'', "\"").lower()

    def __repr__(self) -> str:
        return str(self)


class Config:
    default_player = Player('127.0.0.1')
    player_dict = {}


def socket_service():
    print("Waiting connection...")
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind(("137.184.236.56", 9199))
        s.listen(10)
    except socket.error as msg:
        print(msg)
        sys.exit(1)

    # start to wait connection
    while True:
        conn, addr = s.accept()
        new_player = Player(addr[0])
        Config.player_dict.update({addr[0]: new_player})
        print(Config.player_dict)
        t = threading.Thread(target=deal_data1, args=(conn, addr))
        t.start()


def deal_data1(conn, addr):
    print(f"Accept new connection from {addr}")
    data = conn.recv(1024)
    while data != "":
        # file_object = open(os.path.join(
            # os.getcwd(), 'server-test-file.txt'), 'rb')
        try:
            # all_the_text = file_object.read()
            print("Raw data from client: \n\t" + data.decode())
            
            # update friend score
            now_player = Config.player_dict[addr[0]]
            if not now_player.fip in Config.player_dict.keys():
                for player in Config.player_dict.values():
                    if not player.matched and player.ip != now_player.ip:
                        now_player.fip = player.ip
                        # print("now_player.fip:\t" + now_player.fip)
                        now_player.matched = True
                        player.fip = now_player.ip
                        player.matched = True
                        Config.player_dict.update({player.ip: player})
                        Config.player_dict.update({now_player.ip: now_player})
                        break

            now_player_score = json.loads(data.decode().split("@")[-1])
            now_player.score = now_player_score["score"]
            print("now_player: \n\t" + str(now_player))
            Config.player_dict.update({addr[0]: now_player})
            if now_player.fip != "":
                Config.player_dict[now_player.fip].fscore = now_player.score
            send_message = "@" + str(now_player).replace("'", '"')
            conn.send(send_message.encode())
            print("Message to client: \n\t" + send_message)


        finally:
            # file_object.close()
            pass
        # print("Waiting connection...")
        data = conn.recv(1024)


if __name__ == "__main__":
    socket_service()
