# IEP Miner

A java implementation of the weakly-supervised model described based on the method in Li et al "Comparable Entity Mining from Comparative Questions". 
http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=6042862 

Allows for extraction of *Indicative Extraction Patterns* (IEP) from a question database through a bootstrapping approach. The patterns can be matched against a sentence to extract comparative pairs which in turn can identify comparative sentences.

Its main operations are `pattern generation` and `pattern evaluation`.
* `pattern generation` generates lexical, generalized and specialized patterns from identified comparative questions. The Lexical patterns are sequential patterns consisting of only words and extra identifier symbols. Generalized patterns are lexical patterns with one or more words replaced with its POS tag. Specialized patterns are generated from lexical and generalized patterns by adding POS tags to its comparative indicators ($c) as a constraint.
* `pattern evaluation` evaluates patterns to generate a *reliability score*. If a patterns *reliability score* is greater than a threshold value, it is considered to be reliable and the pattern is kept for the next iteration.

For this work I used a dataset from the Yahoo Research Alliance Webscope program consisting of more than 6 million questions gathered from Yahoo Answers. As the complexity of this application is very high, it's only realistic to run with a set of about 10,000 to 100,000 questions.  

##Differences from the original implementation

The implementation details in the paper is described very loosely and it has been very much up to me to interpret how the different parts of the program is supposed to work. I have done my best to make as accurate implementation as possible however in some cases it has simply not been possible or I don't know what kind of implementation they have done. Here is the most obvious deviations from the implementation details described in the paper.

* The part-of-speech tagger. The implementation in the paper uses NLC-PosTagger which is developed by NLC group of Microsoft Research Asia. After thorough searching I have been unable to find any implementation of this or any further details. Instead I chose to go with the Stanford POS-tagger http://nlp.stanford.edu/software/tagger.shtml which is considered to be very good. However I do not know how well it compares with the other tagger used.
* Phrase chunking. The paper describes in a short table a method they call phrase chunking where they change the POS tag of a word (example NPS -> NP) or merge words and mark them as one through a POS tag (example More+ADJ -> JJR). The paper lists six rules that the phrase chunker adheres to, however this list is non-exhaustive as the list ends with ellipsis. I have no idea what or how many rules are than the original six and any attempt to contact the authors has failed. I did however implement the six rules described in the table.

I'm sure that there might be more deviations than this, but because of the very non-specific descriptions of the implementation, I can't say for sure.

##Evaluation
To evaluate the performance of my implementation I manually labeled a random 100 questions from each Yahoo Answers category except the Yahoo Products category. This gave me a total of 2500 questions of which 1037 questions where comparative. For the comparative questions I also tagged the comparative pairs for each sentence. The dataset used for testing contained 10,000 questions including the 2500 tagged questions. I measured the precision, recall and f-score of comparative question identification and I got the following results:

* Precision: 0.888
* Recall: 0.054
* F-score: 0.101

which is very much worse than the results I should have been getting according to the paper (f-score of 0.817). 

##Installation requirements.
This project depends on the Stanford Log-linear Part-Of-Speech Tagger http://nlp.stanford.edu/software/tagger.shtml. For Lexical pattern generation a Java implementation of  Kekkonen's Generalized Suffix Tree https://github.com/abahgat/suffixtree is used. However I have forked this project and added some extra classes so you should download https://github.com/GitHug/suffixtree. 

If there is any problems with getting this application running, let me know!

## Future work
Right now this project is a bit on hold as I'm working on my thesis. The application was supposed to be used as a baseline for improvements in the field of comparative pair extraction. However as the time I have is limited until the end of my master's project and the current state of the application, I have decided to abandon it for now. Please feel free to fork it or come with suggestions for improvements!

## License

This IEP Miner is released under the Apache License 2.0

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
