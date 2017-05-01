#include <bits/stdc++.h>

using namespace std;

int atual;//indice mapeado da thread que deve executar atualmente
map<thread::id, int> mapa;
string s;
string possivel;//possiveis letras: 'a'...'z' 'A'...'Z'

bool ok(){
	for (int i = 0; i < s.size(); i++)
	{
		if(islower(s[i])) return false;
	}
	return true;
}

int altera(){
	for (int i = 0; i < s.size(); i++)
	{
		if(islower(s[i])){
			s[i] = toupper(s[i]);
			return i;
		}
	}
}

void func(){
	while (!ok())//enquanto tiver letra minuscula
	{
		if(atual==mapa[this_thread::get_id()]){//se for a vez da thread atual
			int pos = altera();//altera e retorna o indice
			
			cout << "Thread " << mapa[this_thread::get_id()] << " alterando o caractere da posicao " << pos << endl;
			cout << "String atual = " << s << endl << endl;
			
			this_thread::sleep_for(chrono::seconds(1));
			
			atual++;
			atual %= 30;
		}
	}
}

int main () {
	srand (time(NULL));
	possivel = "";
	for (char i = 'a'; i <= 'z'; i++)
	{
		possivel += i;
	}
	for (char i = 'a'; i <= 'z'; i++)//criei esse segundo for para preencher a string com as letras possiveis de tal forma que 2/3 dela seja de letras minúsculas, para aumentar a probabilidade de ter letras minúsculas na string aleatória.
	{
		possivel += i;
	}
	for (char i = 'A'; i <= 'Z'; i++)
	{
		possivel += i;
	}
	
	s = "";
	for (int i = 0; i < 80; i++)
	{
		s += possivel[rand()%78];
	}
	
	cout << "String inicial = " << s << endl << endl;
	atual = 0;
	thread t[30];
	for (int i = 0; i < 30; i++)
	{
		t[i] = thread(func);
		mapa[t[i].get_id()] = i;//mapeamento de thread::id pra int
	}
	
	for (int i = 0; i < 30; i++)
	{
		t[i].join();
	}
	
	
	return 0;
}
