# GrammarParser

Built a Recursive Descent Grammar Parser to parse a LL grammar.


The following code is based on the grammar as follows.
<program> ::={<statement_list>}
<statement_list> ::=<statement>;<statement_list’>
<statement_list’> ::=<statement_list>
<statement_list’> ::=ε
<statement> ::=call: <procedure call>
<statement> ::=compute: <expression>
<procedure_call> ::=id(<parameters>)
 <parameters> ::=<factor><parameters’>
 <parameters’> ::=,<parameters>
<parameters’> ::=ε
<expression> ::=id=<factor><expression’>
<expression’> ::=+<factor>
<expression’> ::=-<factor>
<expression’> ::=ε
<factor> ::=id
<factor> ::=num
