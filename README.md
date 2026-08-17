# Padronização do Commit para o Sistema IntegraERP

Para realizar um commit no sistema primeiramente precisamos definir onde estamos, ou seja qual tela estamos trabalhando, então toda vez que  for gerado um commit para um novo recurso que é adicionado/ajustado, no sistema é preciso informar onde você está, então começamos informando em qual tela você está e qual guia da mesma você está trabalhando.<br>
**Exemplo:** adição do novo recurso tornar o campo nome obrigatório para ser cadastrado. Começamos informando a tela, no caso Clientes e depois em qual guia de clientes será adicionado esse novo recurso, nesse caso em Cadastrar Clientes, portanto o commit, irá iniciar assim:<br>

## Clientes  -> Cadastrar Clientes:<br>
A seguir, na imagem é demonstrado a localização do commit citado anteriormente.

<img width="1370" height="768" alt="Image" src="https://github.com/user-attachments/assets/ed5ed38e-4c42-4a0a-a164-92a5a8cdf391" /><br>

Após isso precisamos informar o que foi adicionado ou ajustado, no nosso exemplo adicionamos um recurso novo de tornar o campo obrigatório, então fazemos assim:<br>

## -> Adicionado, recurso de tornar o campo nome obrigatório para o preenchimento.<br>

Perceba que o commit é simples e demonstra de forma clara e resumida **O QUE ESSE NOVO RECURSO FAZ**, ao iniciar o texto coloque a seta  de espaço e logo em seguida  inicie a  primeira letra de todo commit  com maiusculo. Quando for adicionar um recurso novo ao sistema prefira utilizar palavras que remetam a estar adicionando algo novo no sistema então palavras como: **ADICIONADO**, **IMPLEMENTADO** OU **ACRESCENTADO**, são interessantes pois remete a esse procedimento, já quando é alterado um recurso já existente no sistema prefira  palavras que remetem alterações como: **ALTERADO**, **AJUSTADO**, **MODIFICADO**, **CORRIGIDO** OU **AJUSTADO**.<br>

## OBSERVAÇÃO:<br>
  Em alguns casos, não conseguimos definir em qual tela tal recurso se refere, pois pode ser a adição de um novo arquivo, alteração do tipo de um atributo ou qualquer coisa que não seja associado diretamente na tela, nesse caso o melhor a se fazer é colocar Geral, para indicar a localização ficando assim:<br>
## Geral:<br>
Por fim uma boa prática que não é obrigatória, mas sim opcional é informar a data no qual foi realizado esse commit, exemplo:<br>

## -> Alterações realizadas em 27/02/2026.<br><br>

# 📌 Regras para Facilitar o Merge entre Branches

1. **Divisão de tarefas por tela**
   Quando uma pessoa estiver trabalhando em uma tela específica (por exemplo, *Venda*), outra pessoa deverá atuar em uma tela diferente, a fim de evitar conflitos durante o processo de merge.

2. **Limite de funcionalidades por semana**
   Recomenda-se subir, no máximo, duas novas funcionalidades por semana, garantindo maior controle, organização e qualidade nas entregas.

3. **Padronização de comentários no código**
   É obrigatório adicionar comentários explicativos no código. Essa prática facilita a identificação de alterações e auxilia na resolução de possíveis conflitos durante o merge.

4. **Criação de branch para funcionalidades extensas**
   Ao desenvolver uma funcionalidade que demande um tempo considerável para conclusão, deverá ser criada uma branch exclusiva para essa implementação.

5. **Manutenção de branch de backup**
   Deve-se manter uma branch de backup atualizada, a fim de prevenir perdas e possibilitar rápida recuperação em caso de imprevistos.


