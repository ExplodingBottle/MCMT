#!/usr/bin/perl

use File::Find;
my $source = $ARGV[0];
my $dest = $ARGV[1];
my $pathdir = $ARGV[2];

unless (defined $ARGV[2]) { print "Usage: $0 <source> <dest> <patch_directory>\n"; exit 0; }
my @alldir;
find sub {
     return if -d;
     push @alldir, $File::Find::name;
}, "$source";
for my $path ( @alldir) {
     my @tmp = split ("/",$path); my $rmt_dir = shift(@tmp); 
     my $fpath = join("/",@tmp);  my $fn = $tmp[-1];
     pop(@tmp); my $strp_path = join("/",@tmp);
     `mkdir -p $pathdir/$strp_path` unless( -d "$pathdir/$strp_path");
     `diff -u $path $dest$fpath > $pathdir/$strp_path/$fn.patch`;
     if (-z "$pathdir/$strp_path/$fn.patch") {
        unlink("$pathdir/$strp_path/$fn.patch");
     }
}