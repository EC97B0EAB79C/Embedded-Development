
T=425

def readFile(filename):
    file=open(filename)
    data=file.readlines()

    ctr=0
    byteValue=0
    for d in data:
        temp=d.strip().split(" ")
        if(temp[0]=="pulse" and round(int(temp[1])/T)==8):
            print("\nnew command")
        elif(temp[0]=="space" and round(int(temp[1])/T)==4):
            pass

        elif(temp[0]=="space"):
            
            #print(temp)
            #print(round(float(temp[1])/T))
            if(round(float(temp[1])/T)==1):
                #print("0",end="")
                ctr+=1
            elif((round(float(temp[1])/T)==3)):
                #print("1",end="")   
                byteValue+=1<<ctr
                ctr+=1
            if(ctr==8):
                ctr=0
                print(byteValue,end=" ")
                byteValue=0
        elif(temp[0]=="timeout"):
            
            pass

    print(ctr)



def main():
    readFile("ac_on1")


if __name__=="__main__":
    main()
