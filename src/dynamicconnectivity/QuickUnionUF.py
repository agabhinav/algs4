'''
Data Structure: integer array id[] of length N
Interpretation: id[i] is parent of i
Root of i is id[id[id[...id[i]...]]]

Find: Check if p and q have the same root

Union: To merge components containing p and q, set the id of p's root to the id of q's root
'''
class QuickFindUF:
    def __init__(self, N):
        self.objects = []
        for i in range(N):
            self.objects.append(i)
        print(self.objects)

    def root(self,i):
        while (i != self.objects[i]):
            i = self.objects[i]
        return i

    def connected(self, p,q):
        return self.root(p) == self.root(q)

    def union(self, p,q) :
        p_root = self.root(p)
        q_root = self.root(q)
        self.objects[p_root] = q_root

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
