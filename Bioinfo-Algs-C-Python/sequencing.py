import sys

def cyclospec(pep):
	aas = pep.split('-')
	cyclo = [0]
	for i in range(1, len(aas)+1):
		for j in range(len(aas)-i+1):
			mass = 0
			for k in range(i):
				mass += int(aas[j+k])
			cyclo += [mass]
	return cyclo

def main():
	spectrum = [int(x) for x in sys.stdin.read().strip().split(' ')]
	peptides = {"":0}
	while peptides:
		for k, v in peptides.items():
			del peptides[k]
			for x in spectrum:
				if x >= 57 and x <= 200:
					if k != "":
						peptides[k+'-'+str(x)] = v + x
					else:
						peptides[str(x)] = v + x
		for k, v in peptides.items():
			#print 'k: ' + k + ' v: ' + str(v)
			cyclo = cyclospec(k)
			#print cyclo
			if v == spectrum[-1]:
				if set(cyclo).issubset(set(spectrum)):
					print k
				else:
					del peptides[k]
			elif not set(cyclo).issubset(set(spectrum)):
				del peptides[k]


if __name__ == '__main__':
    main()


		