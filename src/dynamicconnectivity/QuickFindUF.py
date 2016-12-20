'''
Data Structure: integer array id[] of length N
Interpretation: p and q are connected iff they have the same id

Find: Check if p and q have the same id

Union: To merge components containing p and q, change all entries whose id equals id[p] to id[q]
'''
class QuickFindUF:
    def __init__(self, N):
        self.objects = []
        for i in range(N):
            self.objects.append(i)

    def connected(self, p,q):
        return self.objects[p] == self.objects[q]

    def union(self, p,q) :
        pid = self.objects[p]
        qid = self.objects[q]
        for i in range(len(self.objects)):
            if (self.objects[i] == pid):
                self.objects[i] = qid


if __name__ == '__main__':
    N = input("Enter N - number of objects: ")
    uf = QuickFindUF(int(N))
    input_pair=[]

    while input_pair != "x":
        input_pair = input("Enter pair of numbers separated with comma (x to terminate): ")
        if (input_pair !="x"):
            input_pair = input_pair.split(sep=",")
            p = int(input_pair [0])
            q = int(input_pair [1])

            if(not uf.connected(p,q)):
                uf.union(p,q)
                print("Connected {p} and {q}".format(p=p, q=q))
            else:
                print("Already connected {p} and {q}".format(p=p, q=q))
