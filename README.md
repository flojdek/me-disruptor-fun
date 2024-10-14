Of course this is super basic example and a TON of things can be optimised.
Like the order book pre-allocation etc.
But just an example how to use Disruptor in a non-trivial manner.
- Like obviously we would not write to files and do stdout in the consumers that would be super slow.
- Same with networking etc.
- No allocations (like obviously no 'clone' etc.) - zero GC - that's where Disruptor works best and these are its principles.
Again just a bit more basic example how to use it.
