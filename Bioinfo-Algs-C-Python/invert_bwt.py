import sys
import string

def rank(list, x):
	indx = 0
	for y in list:
		if x == y:
			break
		indx += 1
	return indx


def main():
	last = sys.stdin.read().strip()
	first = sorted(last)

	invert = "$"

	indx = 0
	for i in range(len(last)-1):
		invert = last[indx] + invert
		rk = rank([i for i, n in enumerate(last) if n == last[indx]], indx)
		indx = [i for i, n in enumerate(first) if n == last[indx]][rk]

	print invert

if __name__ == '__main__':
    main()